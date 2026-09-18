import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';

import { useAuth } from '../context/AuthContext';
import { apiFetch } from '../services/api';

const emptyForm = {
  nombre: '',
  email: '',
  rol: 'usuario',
  cargo: '',
  password: '',
};

export default function DashboardPage() {
  const navigate = useNavigate();
  const { user, logout } = useAuth();
  const [employees, setEmployees] = useState([]);
  const [loading, setLoading] = useState(true);
  const [pageError, setPageError] = useState('');
  const [formError, setFormError] = useState('');
  const [form, setForm] = useState(emptyForm);
  const [isEditing, setIsEditing] = useState(false);
  const [editingId, setEditingId] = useState(null);
  const [isFormOpen, setIsFormOpen] = useState(false);
  const [deleteTarget, setDeleteTarget] = useState(null);

  const loadEmployees = async () => {
    try {
      setLoading(true);
      const response = await apiFetch('/empleados');
      setEmployees(response?.data || []);
      setPageError('');
    } catch (err) {
      setPageError(err.message || 'No se pudieron cargar los empleados.');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadEmployees();
  }, []);

  const handleInputChange = (event) => {
    const { name, value } = event.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  };

  const openCreateModal = () => {
    setIsEditing(false);
    setEditingId(null);
    setForm({ ...emptyForm, rol: 'usuario' });
    setFormError('');
    setIsFormOpen(true);
  };

  const openEditModal = (employee) => {
    setIsEditing(true);
    setEditingId(employee.id);
    setForm({
      nombre: employee.nombre,
      email: employee.email,
      rol: employee.rol,
      cargo: employee.cargo,
      password: '',
    });
    setFormError('');
    setIsFormOpen(true);
  };

  const validateForm = () => {
    if (!form.nombre.trim()) return 'El nombre es obligatorio.';
    if (!form.email.trim()) return 'El email es obligatorio.';

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(form.email.trim())) return 'El email no es válido.';

    if (!form.rol || !['admin', 'usuario'].includes(form.rol)) {
      return 'El rol debe ser admin o usuario.';
    }

    if (!form.cargo.trim()) return 'El cargo es obligatorio.';

    if (!isEditing && !form.password.trim()) return 'La contraseña es obligatoria.';

    return '';
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    const validationMessage = validateForm();

    if (validationMessage) {
      setFormError(validationMessage);
      return;
    }

    try {
      const payload = {
        nombre: form.nombre.trim(),
        email: form.email.trim(),
        rol: form.rol,
        cargo: form.cargo.trim(),
        ...(form.password ? { password: form.password } : {}),
      };

      let response;
      if (isEditing) {
        response = await apiFetch(`/empleados/${editingId}`, {
          method: 'PUT',
          body: JSON.stringify(payload),
        });
        setEmployees((prev) =>
          prev.map((employee) =>
            employee.id === editingId ? response.data : employee,
          ),
        );
      } else {
        response = await apiFetch('/empleados', {
          method: 'POST',
          body: JSON.stringify(payload),
        });
        setEmployees((prev) => [response.data, ...prev]);
      }

      setIsFormOpen(false);
      setForm(emptyForm);
      setFormError('');
    } catch (err) {
      setFormError(err.message || 'No se pudo guardar el empleado.');
    }
  };

  const handleDelete = async () => {
    if (!deleteTarget) return;

    try {
      await apiFetch(`/empleados/${deleteTarget.id}`, {
        method: 'DELETE',
      });
      setEmployees((prev) => prev.filter((employee) => employee.id !== deleteTarget.id));
      setDeleteTarget(null);
    } catch (err) {
      setPageError(err.message || 'No se pudo eliminar el empleado.');
    }
  };

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <div className="dashboard-shell">
      <header className="topbar">
        <div>
          <p className="eyebrow">Panel administrativo</p>
          <h1>Empleados</h1>
        </div>

        <div className="topbar-actions">
          <span className="user-pill">{user?.email}</span>
          <button className="secondary-btn" type="button" onClick={handleLogout}>
            Cerrar sesión
          </button>
        </div>
      </header>

      <div className="toolbar">
        <button type="button" className="primary-btn" onClick={openCreateModal}>
          + Crear empleado
        </button>
      </div>

      {pageError && <div className="alert error">{pageError}</div>}

      {loading ? (
        <div className="state-box">Cargando empleados...</div>
      ) : (
        <div className="table-card">
          <table>
            <thead>
              <tr>
                <th>ID</th>
                <th>Nombre</th>
                <th>Email</th>
                <th>Rol</th>
                <th>Cargo</th>
                <th>Acciones</th>
              </tr>
            </thead>
            <tbody>
              {employees.length === 0 ? (
                <tr>
                  <td colSpan="6" className="empty-row">
                    No hay empleados registrados.
                  </td>
                </tr>
              ) : (
                employees.map((employee) => (
                  <tr key={employee.id}>
                    <td>{employee.id}</td>
                    <td>{employee.nombre}</td>
                    <td>{employee.email}</td>
                    <td>{employee.rol}</td>
                    <td>{employee.cargo}</td>
                    <td>
                      <div className="row-actions">
                        <button
                          type="button"
                          className="table-btn edit"
                          onClick={() => openEditModal(employee)}
                        >
                          Editar
                        </button>
                        <button
                          type="button"
                          className="table-btn delete"
                          onClick={() => setDeleteTarget(employee)}
                        >
                          Eliminar
                        </button>
                      </div>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      )}

      {isFormOpen && (
        <div className="modal-backdrop" onClick={() => setIsFormOpen(false)}>
          <div className="modal-card" onClick={(event) => event.stopPropagation()}>
            <div className="modal-header">
              <h2>{isEditing ? 'Actualizar empleado' : 'Crear empleado'}</h2>
              <button type="button" className="close-btn" onClick={() => setIsFormOpen(false)}>
                ×
              </button>
            </div>

            <form className="employee-form" onSubmit={handleSubmit} noValidate>
              <div className="form-grid">
                <label>
                  <span>Nombre</span>
                  <input
                    type="text"
                    name="nombre"
                    value={form.nombre}
                    onChange={handleInputChange}
                  />
                </label>

                <label>
                  <span>Email</span>
                  <input
                    type="email"
                    name="email"
                    value={form.email}
                    onChange={handleInputChange}
                  />
                </label>

                <label>
                  <span>Rol</span>
                  <select name="rol" value={form.rol} onChange={handleInputChange}>
                    <option value="usuario">usuario</option>
                    <option value="admin">admin</option>
                  </select>
                </label>

                <label>
                  <span>Cargo</span>
                  <input
                    type="text"
                    name="cargo"
                    value={form.cargo}
                    onChange={handleInputChange}
                  />
                </label>

                {!isEditing && (
                  <label className="full-width">
                    <span>Contraseña</span>
                    <input
                      type="password"
                      name="password"
                      value={form.password}
                      onChange={handleInputChange}
                    />
                  </label>
                )}
              </div>

              {formError && <div className="alert error">{formError}</div>}

              <div className="modal-actions">
                <button type="button" className="secondary-btn" onClick={() => setIsFormOpen(false)}>
                  Cancelar
                </button>
                <button type="submit" className="primary-btn">
                  {isEditing ? 'Guardar cambios' : 'Crear empleado'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

      {deleteTarget && (
        <div className="modal-backdrop" onClick={() => setDeleteTarget(null)}>
          <div className="modal-card confirm" onClick={(event) => event.stopPropagation()}>
            <h2>Confirmar eliminación</h2>
            <p>
              ¿Seguro que deseas eliminar al empleado <strong>{deleteTarget.nombre}</strong>?
            </p>
            <div className="modal-actions">
              <button type="button" className="secondary-btn" onClick={() => setDeleteTarget(null)}>
                Cancelar
              </button>
              <button type="button" className="danger-btn" onClick={handleDelete}>
                Eliminar
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
