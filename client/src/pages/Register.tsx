import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuthStore } from '../store/useAuthStore';
import { Coffee } from 'lucide-react';

export const Register = () => {
  const [username, setUsername] = useState('');
  const [fullName, setFullName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const register = useAuthStore((state) => state.register);
  const navigate = useNavigate();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (username && fullName && email && password) {
      setIsLoading(true);
      setError('');
      try {
        await register(email, password, username, fullName);
        // After successful registration, usually we navigate to login
        navigate('/login');
      } catch (err: any) {
        setError(err.message || 'Failed to register');
      } finally {
        setIsLoading(false);
      }
    }
  };

  return (
    <div className="min-h-screen bg-background flex flex-col justify-center py-12 sm:px-6 lg:px-8">
      <div className="sm:mx-auto sm:w-full sm:max-w-md flex flex-col items-center">
        <div className="size-16 rounded-full bg-primary flex items-center justify-center text-background mb-4">
          <Coffee className="size-8" />
        </div>
        <h2 className="text-center text-3xl font-extrabold text-white">Create a new account</h2>
        <p className="mt-2 text-center text-sm text-gray-400">
          Or{' '}
          <Link to="/login" className="font-medium text-primary hover:text-primary/80">
            sign in to your account
          </Link>
        </p>
      </div>

      <div className="mt-8 sm:mx-auto sm:w-full sm:max-w-md">
        <div className="bg-surface py-8 px-4 shadow sm:rounded-lg sm:px-10 border border-primary/10">
          <form className="space-y-6" onSubmit={handleSubmit}>
            {error && (
              <div className="p-3 bg-red-500/10 border border-red-500/50 rounded-md">
                <p className="text-sm text-red-500">{error}</p>
              </div>
            )}
            <div>
              <label className="block text-sm font-medium text-gray-300">Username</label>
              <div className="mt-1">
                <input
                  type="text"
                  required
                  value={username}
                  onChange={(e) => setUsername(e.target.value)}
                  className="appearance-none block w-full px-3 py-2 border border-primary/20 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-primary focus:border-primary sm:text-sm bg-background text-white"
                />
              </div>
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-300">Full Name</label>
              <div className="mt-1">
                <input
                  type="text"
                  required
                  value={fullName}
                  onChange={(e) => setFullName(e.target.value)}
                  className="appearance-none block w-full px-3 py-2 border border-primary/20 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-primary focus:border-primary sm:text-sm bg-background text-white"
                />
              </div>
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-300">Email address</label>
              <div className="mt-1">
                <input
                  type="email"
                  required
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  className="appearance-none block w-full px-3 py-2 border border-primary/20 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-primary focus:border-primary sm:text-sm bg-background text-white"
                />
              </div>
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-300">Password</label>
              <div className="mt-1">
                <input
                  type="password"
                  required
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  className="appearance-none block w-full px-3 py-2 border border-primary/20 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-primary focus:border-primary sm:text-sm bg-background text-white"
                />
              </div>
            </div>

            <div>
              <button
                type="submit"
                disabled={isLoading}
                className="w-full flex justify-center py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-background bg-primary hover:bg-primary/90 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary transition-colors disabled:opacity-50"
              >
                {isLoading ? 'Registering...' : 'Register'}
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};
